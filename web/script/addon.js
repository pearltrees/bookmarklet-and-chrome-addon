
var PEARLTREES_URL = /*@url*/
        //"http://ptrees.hd.free.fr/dev/";
    //"http://www.broceliand.fr:81/dev/";
    "http://cobalt:8080/oyster/";
/*@end*/

var COLLECTOR_URL = PEARLTREES_URL + "s/collectorChrome/";

function getElementUrl(file) {
	return chrome.extension.getURL(file);
}

function setButtonIcon(forceGo, startAnim) {
	chrome.extension.getBackgroundPage().setButtonIcon(forceGo, startAnim);
}

function backgroundRefreshTrees(force) {
	chrome.extension.getBackgroundPage().refreshTrees(force);
}

function getUserTreesData() {
	return chrome.extension.getBackgroundPage().getBackgroundUserTreesData();
}

function getSelectedTree() {
	return chrome.extension.getBackgroundPage().getBackgroundSelectedTree();
}

function setSelectedTree(id, assoId) {
	chrome.extension.getBackgroundPage().setBackgroundSelectedTree(id, assoId);
}

function getTab(onTab) {
	chrome.tabs.getSelected(null, function(tab) {
    	onTab(tab, tab.id, tab.url, tab.title);
	});
}

function retrieveSize(onSize) {
	chrome.windows.getCurrent(function(win) {
		onSize(Math.min(Math.max(win.height * 4 / 5, 200), 600),340);
	});
}

function pearlContent(tab, url, title, treeId, parentTreeId, newTreeName) {
	chrome.extension.getBackgroundPage().pearlContent(tab, url, title, treeId, parentTreeId, newTreeName);
}

function openInNewTab(href) {
	chrome.extension.getBackgroundPage().backgroundOpenInNewTab(href);
}

function reveal(treeId, assoId, tabId, url) {
	chrome.extension.getBackgroundPage().backgroundReveal(treeId, assoId, tabId, url);
}

function goToAccountSync(tabId, url) {
   chrome.extension.getBackgroundPage().goToAccountSync(tabId, url);
}

function onPopupRefresh(login) {
   chrome.extension.getBackgroundPage().onPopupRefresh(login);
}
