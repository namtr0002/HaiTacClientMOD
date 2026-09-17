public class GlobalLogicHandler
{
	public static GlobalLogicHandler instance;

	public static GlobalLogicHandler gI()
	{
		if (instance == null)
		{
			instance = new GlobalLogicHandler();
		}
		return instance;
	}

	public void onConnectFail()
	{
		// GOI TU BG THREAD - chi set flag
		if (GameCanvas.currentScreen == GameCanvas.updateImageAndroidScr) return;
		if (AThMadaraMOD.isAutoReconnect && GameCanvas.currentScreen == GameCanvas.gameScr && GameScreen.player != null)
		{
			AThMadaraMOD.triggerAutoReconnect();
			return;
		}
		string info = (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.Length > 10)
			? GameCanvas.infoDisConnect : T.connectfail;
		GameCanvas.infoDisConnect = "";
		AThMadaraMOD.pendingDisconnectMsg = info;
		AThMadaraMOD.pendingDisconnect = true;
	}

	public void onConnectOK()
	{
	}

	public static void onDisconnect()
	{
		// GOI TU BG THREAD - chi set flag, KHONG goi UI
		mSystem.outz("disconnect global");
		if (AThMadaraMOD.isAutoReconnect && GameCanvas.currentScreen == GameCanvas.gameScr && GameScreen.player != null)
		{
			AThMadaraMOD.triggerAutoReconnect();
			return;
		}
		string msg = (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.Length > 10)
			? GameCanvas.infoDisConnect : T.disconnect;
		GameCanvas.infoDisConnect = "";
		AThMadaraMOD.pendingDisconnectMsg = msg;
		AThMadaraMOD.pendingDisconnect = true;
	}
}
