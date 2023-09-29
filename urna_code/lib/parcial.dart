class Parcial {
  final int totalDeUrnasApuradas;
  final List<Votos> totalDeVotos;

  Parcial(this.totalDeUrnasApuradas, this.totalDeVotos);

  Map<String, dynamic> toJson() => {
        'totalDeUrnasApuradas': totalDeUrnasApuradas,
        'totalDeVotos': totalDeVotos,
      };

  Parcial.fromJson(Map<String, dynamic> json)
      : totalDeUrnasApuradas = json['totalDeUrnasApuradas'],
        totalDeVotos = (json['totalDeVotos'] as List)
            .map((e) => Votos.fromJson(e))
            .toList();

  @override
  toString() => 'Parcial: $totalDeUrnasApuradas, $totalDeVotos';
}

class Votos {
  final int candidato;
  final int votos;

  Votos(this.candidato, this.votos);

  Map<String, dynamic> toJson() => {
        'candidato': candidato,
        'votos': votos,
      };

  Votos.fromJson(Map<String, dynamic> json)
      : candidato = json['candidato'],
        votos = json['votos'];

  @override
  toString() => 'Votos: $candidato, $votos';
}
