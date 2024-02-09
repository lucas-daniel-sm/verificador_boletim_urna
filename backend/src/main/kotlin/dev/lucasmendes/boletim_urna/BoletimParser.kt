package dev.lucasmendes.boletim_urna

import org.springframework.stereotype.Component

@Component
class BoletimParser {
    fun parse(qrcodeData: String): BoletimData {
        val data = mutableMapOf<String, String>()
        val votosMap = mutableMapOf<Int, Int>()

        val fields = qrcodeData.split("\\s+".toRegex())
        for (field in fields) {
            if (field.startsWith("QRBU")) {
                val values = field.split(":")
                val chave = values[0]
                data["$chave.indice"] = values[1]
                data["$chave.total"] = values[2]
            } else {
                val parts = field.split(":")
                if (parts.size == 2) {
                    val chave = parts[0]
                    val valor = parts[1]
                    data[chave] = valor
                }
            }
        }

        data.forEach { (chave, valor) ->
            if (chave.matches("\\d+".toRegex())) {
                val candidato = chave.toInt()
                val votosCandidato = valor.toInt()
                votosMap[candidato] = votosMap.getOrDefault(candidato, 0) + votosCandidato
            }
        }

        val votos = votosMap.map { (candidato, votos) ->
            Voto(candidato = candidato, votos = votos)
        }

        return BoletimData(
            totalVotos = votosMap.values.sum(),
            votos = votos,
            indiceIndice = data["QRBU.indice"] ?: "",
            indiceTotal = data["QRBU.total"] ?: "",
            municipio = data["MUNI"] ?: "",
            zona = data["ZONA"] ?: "",
            secao = data["SECA"] ?: "",
            urna = data["IDUE"] ?: ""
        )
    }
}
