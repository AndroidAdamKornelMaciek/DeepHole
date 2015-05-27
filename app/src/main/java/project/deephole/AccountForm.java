package project.deephole;

public class AccountForm {

	private int id;
	private String name;
	private String password;
	private String email;
	private String phone;
	private String pesel;

	public AccountForm() {}

	public AccountForm(int id, String name, String password, String email, String photo, String pesel) {
		this.id = id;
		this.name = name;
        this.password = password;
        this.email = email;
        this.phone = photo;
        this.pesel = pesel;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	@Override
	public String toString() {
		return "AccountForm [ID = " + id + ", Nazwa = " + name + ", Hasło = " + password +
				", EMail = " + email + ", Telefon = " + phone +
				", Pesel = " + pesel + "]";
	}
}
