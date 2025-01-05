package agh.ics.oop.factory;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalFactory {
    private final AnimalConfiguration animalConfiguration;
    private final Random random = new Random();

    public AnimalFactory(AnimalConfiguration animalConfiguration) {
        this.animalConfiguration = animalConfiguration;
    }

    public Animal createAnimal(Vector2d position) {
        var genome = createGenome();
        return new Animal(animalConfiguration.getStartEnergy(), position, genome);
    }

    public Animal birthAnimal(Animal parent1, Animal parent2, int startBirthEnergy) {
        var dominating = parent1.getEnergy() > parent2.getEnergy() ? parent1 : parent2;
        var other = dominating == parent1 ? parent2 : parent1;
        var percentage = (double) dominating.getEnergy() / other.getEnergy();
        var dominatingLeft = random.nextInt(2) == 1;
        var dominatingGensSize = (int) Math.ceil(percentage * animalConfiguration.getGenomeLength());
        var otherGensSize = animalConfiguration.getGenomeLength() - dominatingGensSize;

        var childGens = new ArrayList<Gen>();
        childGens.addAll(dominating.getGensForChild(dominatingGensSize, dominatingLeft));
        childGens.addAll(other.getGensForChild(otherGensSize, !dominatingLeft));

        var childGenome = new Genome(childGens, animalConfiguration.getMutationVariant());
        childGenome.mutate(random.nextInt(
                animalConfiguration.getMinimumMutationCount(),
                animalConfiguration.getMaximumMutationCount() + 1
        ));

        var child = new Animal(startBirthEnergy,
                parent1.getPosition(), childGenome);

        //TODO add to children

        return child;
    }

    private Genome createGenome() {
        List<Gen> gens = new ArrayList<>();
        for (int i = 0; i < animalConfiguration.getGenomeLength(); i++) {
            gens.add(new Gen(random.nextInt(0, MapDirection.values().length)));
        }

        return new Genome(gens, animalConfiguration.getMutationVariant());
    }
}
