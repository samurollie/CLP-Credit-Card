package com.clp.models

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import java.util.Date

@AllArgsConstructor
@Getter
@Setter
class FaturaModel() {
    var dataDeEmissao: Int;
    var dataDeVencimento: Date;
    var valorTotal: Double;
    var percentualJuros: Double;
    var percentualMulta: Double;
    var valorPago: Double;
    var status: boolean;
    var compras: CompraModel[];

}