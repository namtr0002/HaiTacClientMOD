using System;

public class HieuUngInfo
{
	public int id;
	public string name;
	public string category;
	public short idEff;
	public byte type; // 0 = DataSkillEff ID, 1..5 = Set +11..+15, 6 = Title, 7 = Fashion, 8 = Mastery/Other
	public byte state; // 1 = Đang Bật, 0 = Đang Tắt
	public string optionsStr;
	public mVector actionButtons = new mVector();

	public HieuUngInfo()
	{
	}

	public HieuUngInfo(int id, string name, string category, short idEff, byte type, byte state, string optionsStr)
	{
		this.id = id;
		this.name = name;
		this.category = category;
		this.idEff = idEff;
		this.type = type;
		this.state = state;
		this.optionsStr = optionsStr;
	}
}
