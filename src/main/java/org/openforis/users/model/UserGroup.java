package org.openforis.users.model;

import org.openforis.users.jooq.tables.pojos.OfUserGroup;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserGroup extends OfUserGroup {

	private static final long serialVersionUID = 1L;

	public enum UserGroupRequestStatus {
		
		PENDING("P"), ACCEPTED("A"), REJECTED("R");
		
		private String code;
		
		private UserGroupRequestStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
		
		public static UserGroupRequestStatus fromCode(String code) {
			for (UserGroupRequestStatus value: values()) {
				if (value.getCode().equals(code)) {
					return value;
				}
			}
			throw new IllegalArgumentException(String.format("UserGroupRequestStatus with code %s not found", code));
		}
	}
	
	public enum UserGroupRole {
		
		OWNER("OWN"), ADMINISTRATOR("ADM"), OPERATOR("OPR"), VIEWER("VWR");
		
		private String code;
		
		private UserGroupRole(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
		
		public static UserGroupRole fromCode(String code) {
			for (UserGroupRole value: values()) {
				if (value.getCode().equals(code)) {
					return value;
				}
			}
			throw new IllegalArgumentException(String.format("UserGroupRole with code %s not found", code));
		}
	}

}
