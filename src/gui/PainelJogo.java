package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import jogo.Entidades.CaixaSuprimento;
import jogo.Entidade;
import jogo.Mapa;
import jogo.Entidades.Parede;

import personagens.Compsognato;
import personagens.Player;
import personagens.TRex;
import personagens.Troodonte;
import personagens.Velociraptor;

public class PainelJogo extends JPanel {

    private final Mapa mapa;

    public PainelJogo(Mapa mapa) {
        this.mapa = mapa;
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,700);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Entidade[][] entidades = mapa.getMapa();
        int tamanhoMapa = mapa.getTamanho();
        int tileSize = Math.min(getWidth() / tamanhoMapa, getHeight() / tamanhoMapa);
        int larguraMapa = tileSize * tamanhoMapa;
        int alturaMapa = tileSize * tamanhoMapa;
        int offsetX = (getWidth() - larguraMapa) / 2;
        int offsetY =(getHeight() - alturaMapa) / 2;

        boolean debugAtivo = mapa.isDebug();
        boolean[][] visivel = debugAtivo ? null : mapa.calcularVisibilidade(mapa.getPlayer());

        for (int linha = 0; linha < entidades.length; linha++){
            for (int coluna = 0; coluna < entidades[linha].length; coluna++){
                Entidade entidade = entidades[linha][coluna];
                int x = coluna * tileSize + offsetX;
                int y = linha * tileSize + offsetY;

                boolean celulaVisivel = debugAtivo || visivel[linha][coluna];
                Image imagem = celulaVisivel ? carregarImagem(obterImagemDaEntidade(entidade)) : carregarImagem("oculto.png");
                g.drawImage(imagem,x,y,tileSize,tileSize,this);
            }
        }
    }


    private String obterImagemDaEntidade(Entidade entidade) {
        if (entidade != null) {
            return entidade.getImagem();
        }
        return "ground.png";
    }


    private Image carregarImagem(String nomeArquivo) {
        String caminho = "./imagens/" + nomeArquivo;
        ImageIcon icone = new ImageIcon(caminho);

        return icone.getImage();
    }
}