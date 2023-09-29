//package dev.lucasmendes.boletim_urna;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.List;
//
//public interface BoletimDataRepositoryV2 extends CrudRepository<BoletimData, Long> {
//
//    @Query("SELECT v.candidato, SUM(v.votos) FROM BoletimData b JOIN b.votos v GROUP BY v.candidato")
//
//    public List<Voto> getSumOfTotalVotosPorCandidato();
//}
