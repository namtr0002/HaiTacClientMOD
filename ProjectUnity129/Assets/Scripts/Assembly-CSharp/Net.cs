using UnityEngine;

internal class Net
{
	public static UnityEngine.Networking.UnityWebRequest www;

	public static IKAction h;

	public static void update()
	{
		if (www != null && www.isDone)
		{
			string text = "";
			if (www.error == null || www.error.Equals(""))
			{
				text = www.downloadHandler.text;
			}
			www = null;
			if (h != null)
			{
				h.perform(text);
			}
		}
	}

	public static void connectHTTP(string link, IKAction h)
	{
		if (www != null)
		{
			Cout.LogError("GET HTTP BUSY");
		}
		Debug.Log("REQUEST " + link);
		www = UnityEngine.Networking.UnityWebRequest.Get(link);
		www.SendWebRequest();
		Net.h = h;
		Debug.Log(www?.ToString() + " @@@@@");
	}
}
