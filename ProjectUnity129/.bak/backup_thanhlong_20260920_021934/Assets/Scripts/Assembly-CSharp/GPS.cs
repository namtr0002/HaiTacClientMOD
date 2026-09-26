using System.Collections;
using UnityEngine;

[System.Reflection.Obfuscation(Exclude = true, ApplyToMembers = false)]
public class GPS : MonoBehaviour
{
	public static string Latitude = "";

	public static string Longitude = "";

	private void Start()
	{
		StartCoroutine(StartLocationService());
	}

	private void Update()
	{
	}

	private IEnumerator StartLocationService()
	{
		if (Input.location.isEnabledByUser)
		{
			Input.location.Start(1f, 1f);
			int maxWait = 20;
			while (Input.location.status == LocationServiceStatus.Initializing && maxWait > 0)
			{
				yield return new WaitForSeconds(1f);
				maxWait--;
			}
			if (maxWait > 0 && Input.location.status != LocationServiceStatus.Failed)
			{
				Latitude = Input.location.lastData.latitude.ToString() ?? "";
				Longitude = Input.location.lastData.longitude.ToString() ?? "";
				StartCoroutine(TrackLocation());
			}
		}
	}

	private IEnumerator TrackLocation()
	{
		while (true)
		{
			yield return new WaitForSeconds(5f);
			if (Input.location.status == LocationServiceStatus.Running)
			{
				Latitude = Input.location.lastData.latitude.ToString() ?? "";
				Longitude = Input.location.lastData.longitude.ToString() ?? "";
			}
			Debug.LogWarning("VO DAY ");
		}
	}
}
