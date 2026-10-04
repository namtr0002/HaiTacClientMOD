using System.Threading;
using UnityEngine;

public class mSound
{
	private const int INTERVAL = 5;

	private const int MAXTIME = 100;

	public static int status;

	public static int postem;

	public static int timestart;

	private static string filenametemp;

	private static float volumetem;

	public static bool isSound = true;

	public static bool isMusic = true;

	public static bool isNotPlay = false;

	public static AudioSource SoundWater;

	public static AudioSource SoundRun;

	public static AudioSource SoundBGLoop;

	public static float volumeSound = 0.7f;

	public static float volumeMusic = 0.8f;

	public static AudioClip[] music;

	public static GameObject[] player;

	public static int l1;

	public static int idCurent = -1;

	public static void stopAll()
	{
		stopAllz();
	}

	public static bool isPlaying()
	{
		return false;
	}

	public static void init()
	{
		GameObject gameObject = new GameObject();
		gameObject.name = "Audio Player";
		gameObject.transform.position = Vector3.zero;
		if (Object.FindObjectOfType<AudioListener>() == null)
		{
			gameObject.AddComponent<AudioListener>();
		}
		SoundBGLoop = gameObject.AddComponent<AudioSource>();
	}

	public static void init(int musicID, int sID)
	{
		if (player == null && music == null)
		{
			init();
			l1 = musicID;
			player = new GameObject[musicID + sID];
			music = new AudioClip[musicID + sID];
			for (int i = 0; i < player.Length; i++)
			{
				getAssetSoundFile((i < l1) ? ("/sound/m" + i) : ("/sound/s" + (i - l1)), i);
			}
		}
	}

	public static void playSound(int id, float volume)
	{
		if (music != null && isSound && id >= 0 && id <= music.Length - l1 - 1)
		{
			play(id + l1, volume);
		}
	}

	public static void playSound1(int id, float volume)
	{
		play(id, volume);
	}

	public static void getAssetSoundFile(string fileName, int pos)
	{
		stop(pos);
		load(Main.res + fileName, pos);
	}

	public static void stopSoundAll()
	{
		if (music == null) return;
		for (int i = 0; i < music.Length; i++)
		{
			stop(i);
		}
	}

	public static void stopAllz()
	{
		if (music == null) return;
		for (int i = 0; i < music.Length; i++)
		{
			stop(i);
		}
		for (int j = 0; j < l1; j++)
		{
			sTopSoundBG(j);
		}
	}

	public static void stopAllBg()
	{
		if (music == null) return;
		for (int i = 0; i < music.Length; i++)
		{
			stop(i);
		}
		sTopSoundBG(0);
		sTopSoundRun();
		stopSoundNatural(0);
	}

	public static void update()
	{
	}

	public static void stopMusic(int x)
	{
		stop(x);
	}

	public static void play(int id, float volume)
	{
		start(volume, id);
	}

	public static void playSoundRun(int id, float volume)
	{
		if (SoundRun != null && music != null && id >= 0 && id < music.Length && music[id] != null)
		{
			AudioSource src = SoundRun.GetComponent<AudioSource>();
			if (src != null)
			{
				src.loop = true;
				src.clip = music[id];
				src.volume = volume;
				src.Play();
			}
		}
	}

	public static void sTopSoundRun()
	{
		if (SoundRun != null)
		{
			AudioSource src = SoundRun.GetComponent<AudioSource>();
			if (src != null) src.Stop();
		}
	}

	public static bool isPlayingSound()
	{
		if (SoundRun == null)
		{
			return false;
		}
		AudioSource src = SoundRun.GetComponent<AudioSource>();
		return src != null && src.isPlaying;
	}

	public static void playSoundNatural(int id, float volume, bool isLoop)
	{
		if (SoundWater != null && music != null && id >= 0 && id < music.Length && music[id] != null)
		{
			AudioSource src = SoundWater.GetComponent<AudioSource>();
			if (src != null)
			{
				src.loop = isLoop;
				src.clip = music[id];
				src.volume = volume;
				src.Play();
			}
		}
	}

	public static void stopSoundNatural(int id)
	{
		if (SoundWater != null)
		{
			AudioSource src = SoundWater.GetComponent<AudioSource>();
			if (src != null) src.Stop();
		}
	}

	public static bool isPlayingSoundatural(int id)
	{
		if (SoundWater == null)
		{
			return false;
		}
		AudioSource src = SoundWater.GetComponent<AudioSource>();
		return src != null && src.isPlaying;
	}

	public static void playMus(int type, float vl, bool loop)
	{
		if (isMusic && type >= 0 && type <= l1 - 1)
		{
			playSoundBGLoop(type, vl);
		}
	}

	public static void playSoundBGLoop(int id, float volume)
	{
		if (SoundBGLoop != null && id != idCurent && music != null && id >= 0 && id < music.Length && music[id] != null)
		{
			AudioSource src = SoundBGLoop.GetComponent<AudioSource>();
			if (src != null)
			{
				src.loop = true;
				src.clip = music[id];
				src.volume = volume;
				src.Play();
				idCurent = id;
			}
		}
	}

	public static void sTopSoundBG(int id)
	{
		if (SoundBGLoop != null)
		{
			AudioSource src = SoundBGLoop.GetComponent<AudioSource>();
			if (src != null) src.Stop();
		}
	}

	public static bool isPlayingSoundBG(int id)
	{
		if (SoundBGLoop == null)
		{
			return false;
		}
		return SoundBGLoop.GetComponent<AudioSource>().isPlaying;
	}

	public static void load(string filename, int pos)
	{
		__load(filename, pos);
	}

	private static void _load(string filename, int pos)
	{
		__load(filename, pos);
	}

	private static AudioSource[] s_audioSources;

	private static void __load(string filename, int pos)
	{
		if (music == null || pos < 0 || pos >= music.Length || player == null || pos >= player.Length) return;
		music[pos] = (AudioClip)Resources.Load(filename, typeof(AudioClip));
		GameObject cam = GameObject.Find("Main Camera");
		if (cam != null)
		{
			if (s_audioSources == null || s_audioSources.Length != player.Length)
			{
				s_audioSources = new AudioSource[player.Length];
			}
			AudioSource aSrc = cam.AddComponent<AudioSource>();
			s_audioSources[pos] = aSrc;
			player[pos] = cam;
		}
	}

	public static void start(float volume, int pos)
	{
		__start(volume, pos);
	}

	public static void _start(float volume, int pos)
	{
		__start(volume, pos);
	}

	public static void __start(float volume, int pos)
	{
		if (music != null && pos >= 0 && pos < music.Length && music[pos] != null)
		{
			if (s_audioSources != null && pos < s_audioSources.Length && s_audioSources[pos] != null)
			{
				s_audioSources[pos].PlayOneShot(music[pos], volume);
			}
			else if (player != null && pos < player.Length && player[pos] != null)
			{
				AudioSource src = player[pos].GetComponent<AudioSource>();
				if (src != null)
				{
					src.PlayOneShot(music[pos], volume);
				}
			}
		}
	}

	public static void stop(int pos)
	{
		__stop(pos);
	}

	public static void _stop(int pos)
	{
		__stop(pos);
	}

	public static void __stop(int pos)
	{
		if (player[pos] != null)
		{
			player[pos].GetComponent<AudioSource>().Stop();
		}
	}
}
