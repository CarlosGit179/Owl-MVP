package br.com.carlos.Owl.enums;

import lombok.Getter;

/**
 * Represents the possible loan statuses.
 * <p>
 * Each status has an associated description.
 * </p>
 */
@Getter
public enum LoanStatus {

    ACTIVE("Active"), RETURNED("Returned");

    private final String description;

    /**
    * Constructor for the LoanStatus enumeration.
    *
    * @param description Description of the loan status.
    */
    LoanStatus(String description) {
        this.description = description;
    }

}
