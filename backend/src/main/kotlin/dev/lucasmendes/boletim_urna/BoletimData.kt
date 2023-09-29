package dev.lucasmendes.boletim_urna

import jakarta.persistence.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Entity
class Voto(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val candidato: Int? = null,
    val votos: Int? = null,
)

class VotoOutput(
    val candidato: Int? = null,
    val votos: Long? = null,
)

@Entity
class BoletimData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val totalVotos: Int? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val votos: List<Voto>? = null,
    val indiceIndice: String? = null,
    val indiceTotal: String? = null,
    val municipio: String? = null,
    val zona: String? = null,
    val secao: String? = null,
    @Column(unique = true)
    val urna: String? = null
) {
    override fun toString(): String {
        return "BoletimData(id=$id, totalVotos=$totalVotos, votos=$votos, indiceIndice=$indiceIndice, indiceTotal=$indiceTotal, municipio=$municipio, zona=$zona, secao=$secao, urna=$urna)"
    }
}

interface BoletimDataRepository : CrudRepository<BoletimData?, Long?> {
    @Query("SELECT new dev.lucasmendes.boletim_urna.VotoOutput(v.candidato, SUM(v.votos)) FROM BoletimData b JOIN b.votos v GROUP BY v.candidato")
    fun getTotalVotosPorCandidato(): MutableIterable<VotoOutput>
}

@Service
class BoletimDataService(private val boletimDataRepository: BoletimDataRepository) {
    fun save(boletimData: BoletimData) {
        boletimDataRepository.save(boletimData)
    }

    fun getAll(): MutableIterable<BoletimData?> {
        return boletimDataRepository.findAll()
    }

    fun deleteAll() {
        boletimDataRepository.deleteAll()
    }

    fun getTotalVotosPorCandidato(): MutableIterable<VotoOutput> {
        val totalVotosPorCandidato = boletimDataRepository.getTotalVotosPorCandidato()
        print(totalVotosPorCandidato)
        return totalVotosPorCandidato
    }
}
