package gameobjects;

// Import de packages java

// Import de packages projet

// Représente une plateforme (qui peut être de différents types).
    public abstract class Plateforme extends GameObject { 

        private double saut; // Constante de saut, différente en fonction de la plateforme
        private String id; // Identifiant de l'image correspondante

        /**
         * @param x
         * @param y
         * @param w
         * @param h
         * @param saut
         * @param id
         */
        public Plateforme(double x, double y, double w, double h, double saut) {
            super(x, y, w, h);
            this.saut = saut;
            this.id = "plateforme.png";
        }

        // Getter & Setter
        public String getId() {
            return id;
        }

        public double getSaut() {
            return saut;
        }

        public void setSaut(double saut) {
            this.saut = saut;
        }

        public void setId(String id) {
            this.id = id;
        }
    }