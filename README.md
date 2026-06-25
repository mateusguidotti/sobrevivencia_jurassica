# Sobrevivência Jurássica
Disciplina: Programação Orientada a Objetos - UFPEL 2026/1

## Como compilar e executar

### Requisitos
- Java JDK 11+ instalado

### Compilar
```bash
cd src
javac -d ../bin *.java
```

### Executar
```bash
cd bin
java Main
```

## Como jogar

- O mapa é 10x10, letras A-J (linhas) e 1-10 (colunas)
- Mova o personagem digitando a posição: ex: `B2`, `A3`, `J10`
- Só é possível mover 1 casa por vez, horizontal ou vertical

### Legenda do mapa
| Símbolo | Significado |
|---------|-------------|
| P | Jogador |
| █ | Parede |
| C | Compsognato |
| T | Troodonte |
| V | Velociraptor |
| R | Tiranossauro Rex |
| X | Caixa de Suprimentos |
| . | Chão visível |

### Mecânicas
- **Visão**: você enxerga horizontal e verticalmente até o primeiro obstáculo
- **Combate**: ao mover para uma célula com dinossauro, o combate inicia
- **Caixas**: contêm Kit Médico, Bastão Elétrico, Dardos ou Compsognato surpresa
- **Fuga**: durante o combate é possível fugir para célula adjacente

### Conceitos de POO aplicados
- **Herança**: Entidade → Dinossauro → Compsognato, Troodonte, Velociraptor, TiranossauroRex
- **Polimorfismo**: mover(), podeSerFerido(), getNome() sobrescritos em cada dinossauro
- **Encapsulamento**: atributos privados/protegidos com getters/setters
- **Composição**: Jogador compõe Arma, Posicao; Tabuleiro compõe Dinossauro e CaixaSuprimentos
- **Interfaces**: Atacavel (receberDano, estaMorto), Movel (mover)
