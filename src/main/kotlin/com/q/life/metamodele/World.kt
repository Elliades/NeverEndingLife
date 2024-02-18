package com.q.projects.metamodele

import com.q.projects.controller.Quadtree

class World(val width: Double, val height: Double) {
    @Volatile private var running = true // Contrôle l'exécution du thread de mise à jour
    var cells: MutableList<Cell> = mutableListOf()
    val quadtree = Quadtree(Rectangle(0.0, 0.0, width, height), 4) // Exemple de capacité

    fun addCell(cell: Cell) {
        cells.add(cell)
        cell.birth()
        quadtree.insert(cell) // Insérer la cellule dans le Quadtree
    }

    fun updateQuadtree() {
        quadtree.clear() // Effacer le Quadtree pour le rafraîchir
        cells.forEach { quadtree.insert(it) } // Réinsérer les cellules
    }

    fun update() {
        updateQuadtree()
        cells.forEach { it.behavior(cells) }
    }

    fun initialize() {
        startWorldUpdater()
    }

    fun startWorldUpdater() {
        Thread {
            while (running) {
                this.update()
                try {
                    Thread.sleep(100) // Temporisation pour contrôler la vitesse de mise à jour
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt() // Assurez-vous que le statut d'interruption est rétabli
                }
            }
        }.start()
    }

    fun stopWorldUpdater() {
        running = false // Utilisé pour arrêter la boucle et terminer le thread
    }
}

object WorldHolder {
    var world: World? = null

}
