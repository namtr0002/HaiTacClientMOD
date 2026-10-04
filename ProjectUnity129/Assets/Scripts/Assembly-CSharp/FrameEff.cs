public class FrameEff
{
	public mVector listPartTop = new mVector();

	public mVector listPartBottom = new mVector();

	public mVector listPartPaint;

	public sbyte xShadow;

	public sbyte yShadow;

	public FrameEff(mVector listtop, mVector listbottom)
	{
		listPartTop = (listtop != null) ? listtop : new mVector();
		listPartBottom = (listbottom != null) ? listbottom : new mVector();
		listPartPaint = new mVector();
		for (int i = 0; i < listPartBottom.size(); i++)
		{
			listPartPaint.addElement(listPartBottom.elementAt(i));
		}
		for (int j = 0; j < listPartTop.size(); j++)
		{
			listPartPaint.addElement(listPartTop.elementAt(j));
		}
	}

	public mVector getListPartPaint()
	{
		return listPartPaint;
	}
}
