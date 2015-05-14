package project.deephole;

//KLASA FORMULARZA, OBIEKTY TEJ KLASY BĘDĄ GENEROWANE PRZY UŻYCIU PRZYCISKU "SUBMIT FORM", NASTĘPNIE PRZETWARZANE PRZEZ BAZĘ DANYCH
public class Form {

	private int id;
	private String photoPath;
	private String description;
	private String recipient;
	private String localization;
	private String signature;
	private String telephone;

	public Form() {}

	public Form(int id, String path, String dsc, String rec, String loc, String sgn, String tel) {
		this.id = id;
		photoPath = path;
		description = dsc;
		recipient = rec;
		localization = loc;
		signature = sgn;
		telephone = tel;
	}

	public long getId() {
		return id;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecipient() {
		return recipient;

	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "Form [ID = " + id + ", Photo_Path = " + photoPath + ", Description = " + description +
				", Recipient = " + recipient + ", Localization = " + localization +
				", Signature = " + signature + ", Telephone = " + telephone + "]";
	}
}
