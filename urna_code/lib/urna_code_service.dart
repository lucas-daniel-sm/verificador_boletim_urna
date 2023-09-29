import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:urna_code/parcial.dart';

class QRCodeData {
  String data;

  QRCodeData(this.data);

  Map<String, dynamic> toJson() => {'data': data};

  QRCodeData.fromJson(Map<String, dynamic> json) : data = json['data'];
}

class UrnaCodeService {
  static const String apiUrl = '192.168.237.126:8080';
  static const String apiUser = 'user';
  static const String apiPassword = '3ced42e0-a2d2-4d0e-ab43-86737ee69744';
  static final String basicAuth =
      'Basic ${base64.encode(utf8.encode('$apiUser:$apiPassword'))}';

  Future<bool> addNewQRCode(QRCodeData qrCodeData) async {
    final response = await http.post(
      Uri.http(apiUrl),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': basicAuth,
      },
      body: jsonEncode(qrCodeData.toJson()),
    );

    if (response.statusCode != 202) {
      print(
          'Failed to add new QR Code: ${response.statusCode}: \n\n\t${response.body}\n');
      throw Exception('Failed to add new QR Code: ${response.statusCode} - ${response.body}');
    }

    print('Added new QR Code: ${response.statusCode}: \n\n\t${response.body}\n');
    return true;
  }

  Future<Parcial> getParcial() async {
    final response = await http.get(
      Uri.http(apiUrl, 'parcial'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': basicAuth,
      },
    );

    if (response.statusCode != 200) {
      print(
          'Failed to get parcial: ${response.statusCode}: \n\n\t${response.body}\n');
      throw Exception('Failed to get parcial');
    }

    return Parcial.fromJson(jsonDecode(response.body));
  }
}
