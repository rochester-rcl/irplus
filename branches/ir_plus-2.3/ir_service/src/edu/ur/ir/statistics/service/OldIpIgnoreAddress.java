package edu.ur.ir.statistics.service;

public class OldIpIgnoreAddress {
	
	public Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getFromAddress1() {
		return fromAddress1;
	}

	public void setFromAddress1(int fromAddress1) {
		this.fromAddress1 = fromAddress1;
	}

	public int getFromAddress2() {
		return fromAddress2;
	}

	public void setFromAddress2(int fromAddress2) {
		this.fromAddress2 = fromAddress2;
	}

	public int getFromAddress3() {
		return fromAddress3;
	}

	public void setFromAddress3(int fromAddress3) {
		this.fromAddress3 = fromAddress3;
	}

	public int getFromAddress4() {
		return fromAddress4;
	}

	public void setFromAddress4(int fromAddress4) {
		this.fromAddress4 = fromAddress4;
	}

	public int getToAddress4() {
		return toAddress4;
	}

	public void setToAddress4(int toAddress4) {
		this.toAddress4 = toAddress4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStoreCounts() {
		return storeCounts;
	}

	public void setStoreCounts(boolean storeCounts) {
		this.storeCounts = storeCounts;
	}

	/** From Ip address part 1 */
	public int fromAddress1;

	/** From Ip address part 2 */
	public int fromAddress2;

	/** From Ip address part 3 */
	public int fromAddress3;

	/** From Ip address part 4 */
	public int fromAddress4;
	
	/** TO Ip address part 4 */
	public int toAddress4;
	
	public String name;
	
	public String description;
	
	/** determine if the ignore ip address should be stored persistently
	 *  if this is set to false the ip address should not be stored*/
	public boolean storeCounts = false;

}
