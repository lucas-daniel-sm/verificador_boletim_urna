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
) {
    override fun toString(): String {
        return "Voto(id=$id, candidato=$candidato, votos=$votos)"
    }
}

class VotoOutput(
    val candidato: Int? = null,
    val votos: Long? = null,
)

class Parcial(
    val totalDeUrnasApuradas: Long? = null,
    val totalDeVotos: List<VotoOutput>? = null,
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
    val urna: String? = null
) {
    override fun toString(): String {
        return "BoletimData(id=$id, totalVotos=$totalVotos, votos=$votos, indiceIndice=$indiceIndice, indiceTotal=$indiceTotal, municipio=$municipio, zona=$zona, secao=$secao, urna=$urna)"
    }
}

interface BoletimDataRepository : CrudRepository<BoletimData?, Long?> {
    @Query("SELECT new dev.lucasmendes.boletim_urna.VotoOutput(v.candidato, SUM(v.votos)) FROM BoletimData b JOIN b.votos v GROUP BY v.candidato")
    fun getTotalVotosPorCandidato(): MutableIterable<VotoOutput>

    // seleciona o total de urnas escaneadas, considerando que o campo urna não é único
    @Query("SELECT COUNT(DISTINCT b.urna) FROM BoletimData b")
    fun getTotalDeUrnasEscaneadas(): Long

    @Query("SELECT b FROM BoletimData b WHERE b.urna = ?1 AND b.indiceIndice = ?2")
    fun findByUrnaAndIndiceIndice(urna: String?, indiceIndice: String?): BoletimData?
}

// execao para quando tentar salvar um BoletimData com urna e indice já existentes
class DuplicatedEntryException(message: String?) : Exception(message)

// execao para quando tentar salvar um BoletimData com sem votos
class EmptyVotosException(message: String?) : Exception(message)

@Service
class BoletimDataService(private val boletimDataRepository: BoletimDataRepository) {
    fun save(boletimData: BoletimData) {
        if (boletimData.votos.isNullOrEmpty()) {
            throw EmptyVotosException("BoletimData sem votos")
        }

        val boletimDataByUrnaAndIndiceIndice =
            boletimDataRepository.findByUrnaAndIndiceIndice(boletimData.urna, boletimData.indiceIndice)
        if (boletimDataByUrnaAndIndiceIndice != null) {
            throw DuplicatedEntryException("BoletimData com urna ${boletimData.urna} e indice ${boletimData.indiceIndice} já existe")
        }
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

    fun getTotalDeUrnasEscaneadas(): Long {
        return boletimDataRepository.getTotalDeUrnasEscaneadas()
    }

    fun getParcial(): Parcial {
        val totalDeUrnasEscaneadas = getTotalDeUrnasEscaneadas()
        val totalVotosPorCandidato = getTotalVotosPorCandidato()
        return Parcial(totalDeUrnasEscaneadas, totalVotosPorCandidato.toList())
    }
}
