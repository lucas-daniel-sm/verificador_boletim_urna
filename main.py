import json
from boletim_parser import BoletimParser

if __name__ == "__main__":
    boletim_parser = BoletimParser("boletim.txt")

    out_data = {
        "urna": boletim_parser.urna,
        "QrCodePorUrna": boletim_parser.indice["total"],
        "QrCodeIndice": boletim_parser.indice["indice"],
        "votos": boletim_parser.votos,
    }

    # escreve o arquivo de saída como json
    with open("out.json", "w") as f:
        json.dump(out_data, f, indent=4)
