package baker.soccer.util.objects;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelCellObject {
	private int cellType;
	private Object cellData;
	private XSSFColor fontColor;
	private XSSFColor fillColor;
	private boolean textBold;
	private boolean textItalics;
	private boolean thinBorder;
	private XSSFColor borderColor;
	private short horizontalAlignment;
	private String dataFormat;

	public ExcelCellObject() {
		this(XSSFCell.CELL_TYPE_BLANK, null);
	}

	public ExcelCellObject(int cellType, Object cellData) {
		super();
		this.cellType = cellType;
		this.cellData = cellData;
	}

	public int getCellType() {
		return cellType;
	}

	public void setCellType(int cellType) {
		this.cellType = cellType;
	}

	public Object getCellData() {
		return cellData;
	}

	public void setCellData(Object cellData) {
		this.cellData = cellData;
	}

	public XSSFColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(XSSFColor fontColor) {
		this.fontColor = fontColor;
	}

	public XSSFColor getFillColor() {
		return fillColor;
	}

	public void setFillColor(XSSFColor fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isTextBold() {
		return textBold;
	}

	public void setTextBold(boolean textBold) {
		this.textBold = textBold;
	}

	public boolean isTextItalics() {
		return textItalics;
	}

	public void setTextItalics(boolean textItalics) {
		this.textItalics = textItalics;
	}

	public boolean isThinBorder() {
		return thinBorder;
	}

	public void setThinBorder(boolean thinBorder) {
		this.thinBorder = thinBorder;
	}

	public XSSFColor getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(XSSFColor borderColor) {
		this.borderColor = borderColor;
	}

	public short getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(short horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	@Override
	public String toString() {
		return "ExcelCellObject [cellType=" + cellType + ", cellData="
				+ cellData + ", fontColor=" + fontColor + ", fillColor="
				+ fillColor + ", textBold=" + textBold + ", textItalics="
				+ textItalics + ", thinBorder=" + thinBorder + ", borderColor="
				+ borderColor + ", horizontalAlignment=" + horizontalAlignment
				+ ", dataFormat=" + dataFormat + "]";
	}
}