package shop;

public class ShopInterestDTO {
	private String interest_id;
	private int interest_product_code;
	private String interest_product_name;
	private String interest_product_img;

	public ShopInterestDTO() {
	}

	/* 생성자 ===================
	*/
	public ShopInterestDTO(String interest_id, int interest_product_code, String interest_product_name,
			String interest_product_img) {
		this.interest_id = interest_id;
		this.interest_product_code = interest_product_code;
		this.interest_product_name = interest_product_name;
		this.interest_product_img = interest_product_img;
	}

	/* 유저 아이디 =======================	
	*/
	public String getInterest_id() {
		return interest_id;
	}
	public void setInterest_id(String interest_id) {
		this.interest_id = interest_id;
	}

	/* 관심상품 코드 ==============================	
	*/
	public int getInterest_product_code() {
		return interest_product_code;
	}
	public void setInterest_product_code(int interest_product_code) {
		this.interest_product_code = interest_product_code;
	}

	/* 관심상품 상품명 ==============================	
	*/
	public String getInterest_product_name() {
		return interest_product_name;
	}
	public void setInterest_product_name(String interest_product_name) {
		this.interest_product_name = interest_product_name;
	}

	/* 관심상품 이미지 ==============================	
	*/
	public String getInterest_product_img() {
		return interest_product_img;
	}
	public void setInterest_product_img(String interest_product_img) {
		this.interest_product_img = interest_product_img;
	}
}