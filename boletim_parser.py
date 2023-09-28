class BoletimParser:
    _data: dict = {}
    _votos: dict = {}

    def __init__(self, file_path: str) -> None:
        self._load_data(file_path)

    @property
    def total_votos(self) -> int:
        """Retorna a quantidade total de votos.

        Returns:
            int: Quantidade total de votos.
        """
        return sum(self._votos.values())

    @property
    def votos(self) -> dict:
        """Retorna a quantidade de votos por candidato.

        Returns:
            dict: Quantidade de votos por candidato.
        """
        return self._votos

    @property
    def indice(self) -> dict:
        """Retorna os índices do boletim, em relação ao total de QRCodes desse boletim.

        Returns:
            dict: Índice do boletim. Ex: {"indice": "1", "total": "2"}
        """
        return self._data["QRBU"]

    @property
    def municipio(self) -> str:
        """Retorna o número do município.

        Returns:
            str: Número do município.
        """
        return self._data["MUNI"]

    @property
    def zona(self) -> str:
        """Retorna o número da zona eleitoral.

        Returns:
            str: Número da zona eleitoral.
        """
        return self._data["ZONA"]

    @property
    def secao(self) -> str:
        """Retorna o número da seção eleitoral.

        Returns:
            str: Número da seção eleitoral.
        """
        return self._data["SECA"]

    @property
    def urna(self) -> str:
        """Retorna o número de série da urna.

        Returns:
            str: Número de série da urna.
        """
        return self._data["IDUE"]

    def _load_data(self, file_path: str) -> None:
        """Carrega os dados do arquivo de boletim.

        Args:
            file_path (str): Caminho do arquivo de boletim.
        """
        with open(file_path, "r", encoding="utf-8") as arquivo_boletim:
            codigo: str = arquivo_boletim.read()
        self._data = self._extrair_dados_boletim(codigo)
        self._votos = self._contar_votos()

    def _extrair_dados_boletim(self, codigo: str) -> dict:
        """Extrai os dados do boletim.

        Args:
            codigo (str): Código do boletim.

        Returns:
            dict: Dados do boletim.
        """
        dados: dict = {}
        campos: list = codigo.split()
        for campo in campos:
            # Se o campo começa com "QRBU" é a marca de início dos dados.
            if campo.startswith("QRBU"):
                # parse os valores como uma lista
                valores: list = campo.split(":")
                # o primeiro valor é o nome do campo,
                # o segundo é o índice e o terceiro é a quantidade total
                chave: str = valores[0]
                dados[chave] = {"indice": valores[1], "total": valores[2]}
                # pula para o próximo campo
                continue
            # para os outros campos, o tratamento é o mesmo
            chave, valor = campo.split(":")
            dados[chave] = valor
        return dados

    def _contar_votos(self) -> dict:
        """Conta a quantidade de votos por candidato.

        Returns:
            dict: Quantidade de votos por candidato.
        """
        resultados: dict = {}
        for chave, valor in self._data.items():
            if chave.isnumeric():
                candidato = int(chave)
                votos = int(valor)
                # Verifica se o candidato já está no dicionário
                # e atualiza a quantidade de votos
                if candidato in resultados:
                    resultados[candidato] += votos
                else:
                    resultados[candidato] = votos
        return resultados
