
function WebSocketCallBack (URI, ErrorCallback, ConnectCallback, DataReadyCallback, DisconnectCallback)
{
	//Call Back
	var Self = this;
	
	// �ݹ� �Լ� ���� üũ
	if (typeof ErrorCallback !== 'function')	
		throw new Error('WebSocketCallBack:: ErrorCallback Not specified');
	if (typeof ConnectCallback !== 'function')
		throw new Error('WebSocketCallBack:: ConnectCallback Not specified');
	if (typeof DataReadyCallback !== 'function')
		throw new Error('WebSocketCallBack:: DataReadyCallback Not specified');
	if (typeof DisconnectCallback !== 'function')
		throw new Error('WebSocketCallBack:: DisconnectCallback Not specified');
		
	this.ErrorCallback = ErrorCallback;
	this.ConnectCallback = ConnectCallback;
	this.DataReadyCallback = DataReadyCallback;
	this.DisconnectCallback = DisconnectCallback;	
	
	// Websocket ���� Ȯ��
	this.IsConnected = false;
	
	// Websocket �� uri����
	if (typeof WebSocket !== "undefined")
		this.Socket = new WebSocket(URI);
	else if (typeof webkitWebSocket !== "undefined")
		this.Socket = new webkitWebSocket(URI);
	else if (typeof mozWebSocket !== "undefined")
		this.Socket = new mozWebSocket(URI);
	else
		throw new Error('WebSocketClient: Browser does not support "WebSocket".');

	if(Self.Socket.readyState == 3) {
		Self.IsConnected = false;
		DisconnectCallback();		
	}
	
	// WebSocket ���� ���� ���� - Connect
	this.Socket.addEventListener("open", Socket_OnOpen, false);
	function Socket_OnOpen (event)
	{
		if (Self.Socket.readyState == 1)
		{
			Self.IsConnected = true;
			ConnectCallback();
		}
	}
	
	this.Socket.addEventListener("error", Socket_OnError, false);
	function Socket_OnError (event)
	{
		if (Self.IsConnected == true)
			ErrorCallback("Socket fault.");
		else
			ErrorCallback("Could not connect to server.");
	}
	
	this.Socket.binaryType = 'arraybuffer';
	
	// WebSocket ���� ���� ���� - Disconnect
	this.Socket.addEventListener("close", Socket_OnClose, false);
	function Socket_OnClose (event)
	{
		if (Self.IsConnected == true && (Self.Socket.readyState == 2 || Self.Socket.readyState == 3))
		{
			Self.IsConnected = false;
			DisconnectCallback();
		}
	}
		
	// WebSocket ���� ���� ��ȯ
	this.GetStatus = GetStatus;
	function GetStatus()
	{
		// Return boolean
		return Self.IsConnected;
	}

	// WebSocket Data ó��
	this.Socket.addEventListener("message", Socket_OnMessage, false);
	function Socket_OnMessage (event)
	{
		Self.DataReadyCallback(event.data);
	}
}
