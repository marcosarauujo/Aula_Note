// Quando o usuario clicar no icone da extensao
chrome.action.onClicked.addListener((tab) => {
    // Salva o ID da aba que queremos gravar (ex: a aba do YouTube)
    chrome.storage.local.set({ targetTabId: tab.id }, () => {
        // Abre a interface como uma janela independente (nao fecha ao clicar fora!)
        chrome.windows.create({
            url: chrome.runtime.getURL("popup.html"),
            type: "popup",
            width: 350,
            height: 400
        });
    });
});