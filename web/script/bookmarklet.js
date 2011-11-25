var myScroll = null;

function getElementUrl(file) {
	return "../../collector/bookmark/" + file;
}

function setButtonIcon(forceGo, startAnim) {
}

function backgroundRefreshTrees(force) {
	refreshTrees(force);
}

//Exported
//function getUserTreesData()

function getSelectedTree() {
	return selectedTree;
}

function getSelectedAsso() {
	return selectedAsso;
}

function setSelectedTree(id, assoId) {
	selectedTree = id;
	selectedAsso = assoId;
}

function getTab(onTab) {
	onTab(null, 0, bookmarkedUrl, bookmarkedTitle);
}

function getFirstDetails(onFirstDetails) {
	onFirstDetails(bookmarkedUrl, bookmarkedTitle);
}

function onPopupRefresh(login) {
}

function retrieveSizeForIpad(onSize) {
	var ih = window.innerHeight;
	var iw = window.innerWidth;
	onSize(ih,iw);
}

function scroll(id) {
	new iScroll(id);
}

function areCookiesEnabled() {
	var cookieEnabled = (navigator.cookieEnabled) ? true : false;

	if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
	{ 
		document.cookie="testcookie";
		cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
	}
	return (cookieEnabled);
}

function retrieveSize(onSize) {
	var ih = 0;
	var iw = 0;
	//IE
	if (typeof( window.innerHeight ) != 'number') {
		//strict mode
		if (!(document.documentElement.clientHeight == 0)) {
			ih = document.documentElement.clientHeight;
			iw = document.documentElement.clientWidth;
		}
		//quirks mode
		else {
			ih = document.body.clientHeight;
			iw = document.body.clientWidth;
		}
	}
	//w3c
	else {
		ih = window.innerHeight;
		iw = window.innerWidth;
		if(iw != 340 || ih != 480){
			// Chrome Mac & Linux don't open the popup with the right size
			setTimeout(function(){
				ih = window.innerHeight;
				iw = window.innerWidth;
				var oh = window.outerHeight;
				var ow = window.outerWidth;
				window.resizeTo(340 + (ow - iw), 480 + (oh - ih));
			},100);
			ih = 480;
			iw = 340;
		}
	}
	onSize(ih,iw);
}

//Exported
//function pearlContent(tab, url, title, treeId, parentTreeId, newTreeName)

function openInNewTab(href) {
	if (window.opener) {
		window.opener.postMessage(href, "*");
	}
}

//Exported
//function reveal(treeId, tabId, url)

//Exported
//function goToAccountSync(tabId, url)

window.addEventListener("message", function(e) {
	onCredentials(e.data);
}, false);

/*domainScriptReplace*/
