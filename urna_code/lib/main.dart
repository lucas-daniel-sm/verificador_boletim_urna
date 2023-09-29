import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:qr_code_scanner/qr_code_scanner.dart';
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
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
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
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: const Center(child: Text('QR Code Scanner')),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: () {
              // Navigate to the QR Reader page and wait for the result
              Navigator.of(context).push(MaterialPageRoute(builder: (context) {
                return const QRViewExample();
              })).then((value) {
                final barcode = value as Barcode;
                // When the QR Reader page is popped, show the value on a SnackBar
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text(
                        'Código de barra escaneado' ?? 'No barcode scanned!'),
                  ),
                );
                if (barcode.code != null) {
                  UrnaCodeService()
                      .addNewQRCode(QRCodeData(barcode.code!))
                      .whenComplete(
                        () => ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Código de barra adicionado!'),
                          ),
                        ),
                      );
                }
              });
            },
            child: const Text('Scan QR'),
          ),
        ),
      ),
    );
  }
}
