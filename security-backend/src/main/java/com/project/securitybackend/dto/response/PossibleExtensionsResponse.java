package com.project.securitybackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PossibleExtensionsResponse {

    private boolean digitalSignature;

    private boolean nonRepudiation;

    private boolean keyAgreement;

    private boolean keyEncipherment;
}
