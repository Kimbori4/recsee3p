	var webSocket, connectCount = 0;
	function onWebSocketConnect(webSocketInfo) {
		var host = "ws://"+webSocketInfo.ip+":"+webSocketInfo.port+webSocketInfo.path;
		
		webSocket = new WebSocket(host);

		webSocket.onopen = function() {
			(new Function('return'+webSocketInfo.openFunction+"();"))
			//eval(webSocketInfo.openFunction + "();");
		}

		webSocket.onmessage = function(msg) {
			if(webSocket != null){
				(new Function('return'+webSocketInfo.messageFunction+"(msg);"))
			}
			//eval (webSocketInfo.messageFunction+"(msg);");
		}

		webSocket.onerror = function(error) {
			if (connectCount < 1) {
				dhtmlx.alert({
					title:		$("#message_title_alert").val(),
					text:		$("#ws_message_exception").val(),
					callback: 	function() {
						connectCount++;
						window.setTimeout(function(){ onWebSocketConnect(webSocketInfo); },5000);
					}
				});
			} else {
				dhtmlx.alert({
					title:		$("#message_title_alert").val(),
					text:		$("#ws_message_connect_fail").val()
				});
			}
		}

		webSocket.onclose = function() {
			connectCount = 0;
			//webSocket = null;
			//onWebSocketConnect(webSocketInfo);
		}
	}

	function onWebSocketClose() {
		connectCount = 0;
		webSocket.close();
	}

	function onWebSocketSend(sendMessage) {
		if(sendMessage == "" ) {
			dhtmlx.alert({title:$("#message_title_alert").val(), text:$("#ws_message_no_message").val()});
			return;
		}

		try {
			webSocket.send(sendMessage);
		} catch (exception) {
			dhtmlx.alert({title:$("#message_title_alert").val(), text:$("#ws_message_send_exception").val()+"<br>Error:"+exception.message});
		}
	}

	var chkSystemTimeout = null;
	function onSystemCheck(chkInfo) {
		var host = "ws://"+chkInfo.ip+":9980"+chkInfo.path;
		try {
			webSocket = new WebSocket(host);

			chkSystemTimeout = window.setTimeout("onSystemCheckTimeout("+chkInfo.timeoutFunction+")", 5000);

			webSocket.onopen = function() {
				window.clearTimeout(chkSystemTimeout);
				chkSystemTimeout = null;

				webSocket = null;
				(new Function('return'+chkInfo.openFunction+"(true);"))
				//eval(chkInfo.openFunction+"(true);");
			}
			webSocket.onerror = function(msg) {
				webSocket = null;
				(new Function('return'+chkInfo.errorFunction+"(false);"))
				//eval(chkInfo.errorFunction+"(false);");
			}
			webSocket.onclose = function() {
				window.clearTimeout(chkSystemTimeout);
				chkSystemTimeout = null;

				webSocket = null;
			}
		} catch (exception) {
			onSystemAddProc(false);
		}
	}

	function onSystemCheckTimeout(returnFunc) {
		webSocket.close();
		(new Function('return'+returnFunc+"(false);"))
		//eval(returnFunc+"(false);");
	}