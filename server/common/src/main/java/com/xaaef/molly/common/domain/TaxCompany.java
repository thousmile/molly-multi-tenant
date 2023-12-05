package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.1
 * @date 2022/3/10 14:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxCompany<T> {

    @Schema(description = "ID")
    private T id;

    @Schema(description = "上级ID")
    private T parentId;

}
