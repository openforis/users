package org.openforis.users.model;

import org.openforis.users.jooq.tables.pojos.OfGroup;

/**
 * 
 * @author S. Ricci
 *
 */
public class Group extends OfGroup implements IdentifiableObject {

	private static final long serialVersionUID = 1L;

	public enum Visibility {
		PUBLIC("PUB"), PRIVATE("PRV");
		
		private String code;
		
		Visibility(String code) {
			this.code = code;
		}
		
		public static Visibility fromCode(String code) {
			for (Visibility value : values()) {
				if (value.getCode().equals(code)) {
					return value;
				}
			}
			throw new IllegalArgumentException(String.format("Invalid code for Group Visibility: %s", code));
		}
		
		public String getCode() {
			return code;
		}
	}
	
}
