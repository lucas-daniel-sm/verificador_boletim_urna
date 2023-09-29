package dev.lucasmendes.boletim_urna

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class Api(val boletimParser: BoletimParser, val boletimDataService: BoletimDataService) {

    @PostMapping
    fun postNewQrCode(@RequestBody qrCodeData: QrCodeData): ResponseEntity<Any> {
        val parse = boletimParser.parse(qrCodeData.data)
        boletimDataService.save(parse)
        return ResponseEntity.accepted().body(parse)
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<Any> {
        val all = boletimDataService.getAll()
        return ResponseEntity.ok(all)
    }

    @GetMapping("/total-votos-por-candidato")
    fun getTotalVotosPorCandidato(): ResponseEntity<Any> {
        val all = boletimDataService.getTotalVotosPorCandidato()
        return ResponseEntity.ok(all)
    }

    @DeleteMapping("/all")
    fun deleteAll(): ResponseEntity<Any> {
        boletimDataService.deleteAll()
        return ResponseEntity.ok().build()
    }
}

data class QrCodeData(val data: String)
