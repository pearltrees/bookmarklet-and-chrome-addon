document.addEventListener("flexCommandEvent", function(event) {	
	var commandName = event.target.getAttribute("commandName");

	if (commandName == "detectPearlbar") {           
		var element = document.createElement("pearlbarCommandEvent");
		element.setAttribute("commandName", "pearlbarIsInstalled");
		document.documentElement.appendChild(element);           

		var event = document.createEvent("Events");
		event.initEvent("pearlbarCommandEvent", true, false);
		element.dispatchEvent(event);
		
		chrome.extension.sendRequest({login: true});
	}
	else if (commandName == "login") {
		chrome.extension.sendRequest({login: true});
	}
}, false);
