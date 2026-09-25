const btnStart = document.getElementById('btnStart');
const btnStop = document.getElementById('btnStop');
const inputTitulo = document.getElementById('tituloAula');
const statusText = document.getElementById('status');

let mediaRecorder = null;
let sessaoId = null;
let audioChunks = []; // Aqui vamos guardar a aula inteira!

// Quando clicar em "Iniciar Gravacao"
btnStart.addEventListener('click', async () => {
    const titulo = inputTitulo.value;
    if (!titulo) {
        alert("Por favor, digite o titulo da aula primeiro!");
        return;
    }

    statusText.innerText = "Criando sessão no backend...";

    try {
        // 1. Cria a sessao no backend Java
        const response = await fetch('http://localhost:8080/api/sessoes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ titulo: titulo })
        });

        if (!response.ok) throw new Error("Erro ao criar sessão");
        const sessao = await response.json();
        sessaoId = sessao.id;

        // 2. Pega o ID da aba que queremos capturar
        const { targetTabId } = await chrome.storage.local.get('targetTabId');

        // 3. Pede ao Chrome para capturar a aba atual
        const streamId = await chrome.tabCapture.getMediaStreamId({ targetTabId: targetTabId });
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: { mandatory: { chromeMediaSource: 'tab', chromeMediaSourceId: streamId } }
        });

        // 4. Toca o audio de volta (pra voce nao ficar surdo)
        const audioCtx = new AudioContext();
        const source = audioCtx.createMediaStreamSource(stream);
        source.connect(audioCtx.destination);

        // 5. Comeca a gravar e guarda no array audioChunks
        audioChunks = [];
        mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm' });

        mediaRecorder.ondataavailable = (event) => {
            if (event.data.size > 0) {
                audioChunks.push(event.data);
            }
        };

        // Quando o gravador parar, ele executa essa logica:
        mediaRecorder.onstop = async () => {
            statusText.innerText = "Enviando áudio para o Whisper... 📡";

            // Junta tudo em um arquivo so
            const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });

            // Divide em pedaços de 10 MB para não estourar o limite da OpenAI (25 MB)
            const CHUNK_SIZE = 10 * 1024 * 1024; // 10 MB em bytes
            const totalParts = Math.ceil(audioBlob.size / CHUNK_SIZE);

            try {
                for (let i = 0; i < totalParts; i++) {
                    const inicio = i * CHUNK_SIZE;
                    const fim = Math.min(inicio + CHUNK_SIZE, audioBlob.size);
                    const pedaco = audioBlob.slice(inicio, fim);

                    statusText.innerText = `Enviando parte ${i + 1} de ${totalParts}... 📡`;

                    const chunkForm = new FormData();
                    chunkForm.append('audio', pedaco, `aula_parte_${i + 1}.webm`);

                    await fetch(`http://localhost:8080/api/sessoes/${sessaoId}/transcricoes`, {
                        method: 'POST',
                        body: chunkForm
                    });
                }

                statusText.innerText = "Resumindo com Gemini... 🧠";

                // Finaliza a sessao e manda o comando para gerar a nota
                await fetch(`http://localhost:8080/api/sessoes/${sessaoId}/finalizar`, { method: 'PUT' });
                await fetch(`http://localhost:8080/api/sessoes/${sessaoId}/notas`, { method: 'POST' });

                statusText.innerText = "Buscando resumo final... 🔍";

                // Puxa a nota recem-criada para exibir na tela
                const notasResponse = await fetch(`http://localhost:8080/api/sessoes/${sessaoId}/notas`);
                if (notasResponse.ok) {
                    const nota = await notasResponse.json();
                    if (nota && nota.conteudo) {
                        document.getElementById('resultadoBox').style.display = 'block';
                        document.getElementById('textoResumo').value = nota.conteudo;
                        statusText.innerText = "Resumo gerado com sucesso! ✅";
                    } else {
                        statusText.innerText = "Resumo veio vazio. ❌";
                    }
                } else {
                    statusText.innerText = "Erro ao buscar resumo final. ❌";
                }

            } catch (error) {
                console.error("Erro final:", error);
                statusText.innerText = "Erro ao processar áudio: " + error.message;
            }
        };

        mediaRecorder.start();
        statusText.innerText = "Gravando aula... 🔴";

        // Muda os botoes
        btnStart.style.display = 'none';
        btnStop.style.display = 'block';
        inputTitulo.disabled = true;

    } catch (error) {
        console.error(error);
        statusText.innerText = "Erro: " + error.message;
    }
});

// Quando clicar em "Parar e Gerar Resumo"
btnStop.addEventListener('click', () => {
    btnStop.disabled = true;
    statusText.innerText = "Finalizando captura... ⏳";

    if (mediaRecorder && mediaRecorder.state !== "inactive") {
        mediaRecorder.stop();
        mediaRecorder.stream.getTracks().forEach(track => track.stop());
    }
});