using System;

public class TitleActionBtn
{
	public sbyte actionId;
	public string name;
	public sbyte style; // 0: Vàng/Gold, 1: Xanh lá/Green, 2: Đỏ/Red

	public TitleActionBtn(sbyte actionId, string name, sbyte style)
	{
		this.actionId = actionId;
		this.name = name;
		this.style = style;
	}
}

public class DanhHieuInfo
{
	public int id;
	public string name;
	public short idEff;
	public int coin;
	public byte state; // 0 = Chưa có, 1 = Đã có, 2 = Đang dùng
	public string optionsStr;
	public mVector actionButtons = new mVector();

	public DanhHieuInfo()
	{
	}

	public DanhHieuInfo(int id, string name, short idEff, int coin, byte state, string optionsStr)
	{
		this.id = id;
		this.name = name;
		this.idEff = idEff;
		this.coin = coin;
		this.state = state;
		this.optionsStr = optionsStr;
	}
}
