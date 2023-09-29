import 'package:flutter/material.dart';
import 'package:urna_code/parcial.dart';
import 'package:urna_code/urna_code_service.dart';

class ParcialView extends StatefulWidget {
  const ParcialView({super.key});

  @override
  State<ParcialView> createState() => _ParcialViewState();
}

class _ParcialViewState extends State<ParcialView> {
  final urnaCodeService = UrnaCodeService();
  late final Future<Parcial> parcialFuture;
  late final Parcial parcial;

  @override
  void initState() {
    super.initState();
    parcialFuture = urnaCodeService.getParcial().then((value) {
      parcial = value;
      print('Carregou a parcial: $value');
      return value;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text('Parcial da Urna Eletrônica')),
        elevation: 2,
      ),
      body: FutureBuilder<Parcial>(
        future: parcialFuture,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return loaded;
          } else if (snapshot.hasError) {
            return const Center(child: Text('Erro ao carregar a parcial'));
          } else {
            return loading;
          }
        },
      ),
    );
  }

  Widget get loading => const Center(
        child: CircularProgressIndicator(),
      );

  Widget get loaded => Column(children: [
        const SizedBox(height: 16),
        Text(
          'Total de urnas apuradas: ${parcial.totalDeUrnasApuradas}',
          style: Theme.of(context).textTheme.headlineSmall,
        ),
        const SizedBox(height: 16),
        ListView.builder(
          shrinkWrap: true,
          itemCount: parcial.totalDeVotos.length,
          itemBuilder: (context, index) {
            final votos = parcial.totalDeVotos[index];
            return ListTile(
              title: Text('Candidato: ${votos.candidato}'),
              subtitle: Text('Votos: ${votos.votos}'),
            );
          },
        )
      ]);
}
