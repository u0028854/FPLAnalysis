package baker.soccer.util.objects;

public class FPLFSMapObject {
	private String fsPlayerName;
	private String fsPlayerPosition;
	private String eplPlayerName;
	private String eplPlayerPosition;
	private String understatPlayerName;
	private String understatPlayerPosition;
	private String fbRefPlayerName;

	public FPLFSMapObject(String fsPlayerName, String fsPlayerPosition,
			String eplPlayerName, String eplPlayerPosition, String understatPlayerName, String understatPlayerPosition, String fbRefPlayerName) {
		super();
		this.fsPlayerName = fsPlayerName;
		this.fsPlayerPosition = fsPlayerPosition;
		this.eplPlayerName = eplPlayerName;
		this.eplPlayerPosition = eplPlayerPosition;
		this.understatPlayerName = understatPlayerName;
		this.understatPlayerPosition = understatPlayerPosition;
		this.fbRefPlayerName = fbRefPlayerName;
	}

	public String getFsPlayerName() {
		return fsPlayerName;
	}

	public void setFsPlayerName(String fsPlayerName) {
		this.fsPlayerName = fsPlayerName;
	}

	public String getFsPlayerPosition() {
		return fsPlayerPosition;
	}

	public void setFsPlayerPosition(String fsPlayerPosition) {
		this.fsPlayerPosition = fsPlayerPosition;
	}

	public String getEplPlayerName() {
		return eplPlayerName;
	}

	public void setEplPlayerName(String eplPlayerName) {
		this.eplPlayerName = eplPlayerName;
	}

	public String getEplPlayerPosition() {
		return eplPlayerPosition;
	}

	public void setEplPlayerPosition(String eplPlayerPosition) {
		this.eplPlayerPosition = eplPlayerPosition;
	}

	public String getUnderstatPlayerName() {
		return understatPlayerName;
	}

	public void setUnderstatPlayerName(String understatPlayerName) {
		this.understatPlayerName = understatPlayerName;
	}

	public String getUnderstatPlayerPosition() {
		return understatPlayerPosition;
	}

	public void setUnderstatPlayerPosition(String understatPlayerPosition) {
		this.understatPlayerPosition = understatPlayerPosition;
	}
	
	public String getFBRefPlayerName() {
		return fbRefPlayerName;
	}

	public void getFBRefPlayerName(String fbRefPlayerName) {
		this.fbRefPlayerName = fbRefPlayerName;
	}

	@Override
	public String toString() {
		return "FPLFSMapObject [fsPlayerName=" + fsPlayerName + ", fsPlayerPosition=" + fsPlayerPosition
				+ ", eplPlayerName=" + eplPlayerName + ", eplPlayerPosition=" + eplPlayerPosition
				+ ", understatPlayerName=" + understatPlayerName + ", understatPlayerPosition="
				+ understatPlayerPosition + ", fbRefPlayerName=" + fbRefPlayerName + "]";
	}
}