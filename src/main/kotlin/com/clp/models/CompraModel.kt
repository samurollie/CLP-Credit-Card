package com.clp.models

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import java.util.Date

@AllArgsConstructor
@Getter
@Setter
class CompraModel {
    var data: Date
    var valor: Double
    var loja: String
}