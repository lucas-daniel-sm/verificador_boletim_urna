import 'package:flutter/material.dart';
import 'package:qr_code_scanner/qr_code_scanner.dart';
import 'package:urna_code/parcial_view.dart';
import 'package:urna_code/qr_reader.dart';
import 'package:urna_code/urna_code_service.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const Home(),
    );
  }
}

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  final urnaCodeService = UrnaCodeService();

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: const Center(child: Text('QR Scaner da Urna Eletrônica')),
          elevation: 2,
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [qrCodeButton, verResultadosButton],
          ),
        ),
      ),
    );
  }

  Widget get verResultadosButton => ElevatedButton(
        onPressed: () {
          // Navigate to the Parcial page
          Navigator.of(context).push(MaterialPageRoute(builder: (context) {
            return const ParcialView();
          }));
        },
        child: const Text('Ver resultados'),
      );

  Widget get qrCodeButton => ElevatedButton(
        onPressed: () {
          // Navigate to the QR Reader page and wait for the result
          Navigator.of(context).push(MaterialPageRoute(builder: (context) {
            return const QRViewExample();
          })).then((value) {
            final barcode = value as Barcode;
            if (barcode.code != null) {
              postQRCode(barcode.code!);
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Código de barra não escaneado'),
                ),
              );
            }
          });
        },
        child: const Text('Ler novo código de barras'),
      );

  void postQRCode(String code) async {
    // When the QR Reader page is popped, show the value on a SnackBar
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('Código de barra escaneado'),
      ),
    );
    urnaCodeService.addNewQRCode(QRCodeData(code)).onError((error, stackTrace) {
      ScaffoldMessenger.of(context).hideCurrentSnackBar();
      print('Erro ao adicionar código de barra\n$error\n$stackTrace');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Erro ao adicionar código de barra\n$error'),
        ),
      );
      return false;
    }).then((result) {
      if (!result) {
        return;
      }
      ScaffoldMessenger.of(context).hideCurrentSnackBar();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Código de barra adicionado!'),
        ),
      );
    });
  }
}
