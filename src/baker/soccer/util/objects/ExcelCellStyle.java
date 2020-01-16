package baker.soccer.util.objects;

import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

public class ExcelCellStyle extends org.apache.poi.xssf.usermodel.XSSFCellStyle {
	public ExcelCellStyle(StylesTable stylesSource){
		super(stylesSource);
	}

	public void setThinBorder(XSSFColor borderColor){
		this.setBorderColor(BorderSide.TOP, borderColor);
		this.setBorderColor(BorderSide.LEFT, borderColor);
		this.setBorderColor(BorderSide.RIGHT, borderColor);
		this.setBorderColor(BorderSide.BOTTOM, borderColor);

		this.setBorderTop(XSSFCellStyle.BORDER_THIN);
		this.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		this.setBorderRight(XSSFCellStyle.BORDER_THIN);
		this.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	}

	public void setPlainFillColor (XSSFColor borderColor){
		this.setFillForegroundColor(borderColor);
		this.setFillBackgroundColor(borderColor);
		this.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
	}
}
