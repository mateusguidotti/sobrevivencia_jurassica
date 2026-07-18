package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import jogo.CaixaSuprimento;
import jogo.Entidade;
import jogo.Mapa;
import jogo.Parede;

import personagens.Compsognato;
import personagens.Player;
import personagens.TRex;
import personagens.Troodonte;
import personagens.Velociraptor;

public class PainelJogo extends JPanel {

    private final Mapa mapa;

    private final Image imagemChao;
    private final Image imagemParede;
    private final Image imagemCaixaSuprimento;
    private final Image imagemPlayer ;
    private final Image imagemVelociraptor;
    private final Image imagemTroodonte;
    private final Image imagemCompsognato;
    private final Image imagemTRex;

    public PainelJogo(Mapa mapa) {
        this.mapa = mapa;

        imagemChao = carregarImagem("ground.png");

        imagemParede = carregarImagem("wall.png");
        imagemCaixaSuprimento = carregarImagem("caixa_suprimentos.png");
        imagemPlayer = carregarImagem("player.png");
        imagemVelociraptor = carregarImagem("velociraptor.png");
        imagemTroodonte = carregarImagem("troodonte.png");
        imagemCompsognato = carregarImagem("compsognato.png");
        imagemTRex = carregarImagem("t_rex.png");
   
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


        for (int linha = 0; linha < entidades.length; linha++){
            for (int coluna = 0; coluna < entidades[linha].length; coluna++){
                Entidade entidade = entidades[linha][coluna];
                int x = coluna * tileSize + offsetX;
                int y = linha * tileSize + offsetY;

                Image imagem = obterImagemDaEntidade(entidade);
                g.drawImage(imagem,x,y,tileSize,tileSize,this);
            }
        }
    }


    private Image obterImagemDaEntidade(Entidade entidade) {
        
        if (entidade  instanceof Player){
            return imagemPlayer;
        }

        if (entidade instanceof Velociraptor) {
            return imagemVelociraptor;
        }
        
        if (entidade instanceof Troodonte) {
            return imagemTroodonte;
        }

        if (entidade instanceof Compsognato) {
            return imagemCompsognato;
        }
        
        if (entidade instanceof TRex) {
            return imagemTRex;
        }
        
        if (entidade instanceof Parede) {
            return imagemParede;
        }

        if (entidade instanceof CaixaSuprimento) {
            return imagemCaixaSuprimento;
        }

        /*
         * Qualquer entidade que não seja:
         *
         * Player
         * Dinossauro
         * Parede
         * CaixaSuprimento
         *
         * será desenhada como chão.
         *
         * Isso também cobre null.
         */

        return imagemChao;
    }


    private Image carregarImagem(
            String nomeArquivo
    ) {

        String caminho =
                "./imagens/"
                        + nomeArquivo;


        ImageIcon icone =
                new ImageIcon(
                        
                                caminho
                        
                );


        return icone.getImage();
    }
}