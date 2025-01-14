package agh.ics.oop.factory;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.exceptions.AnimalNotBirthException;
import agh.ics.oop.model.exceptions.InvalidCountException;

import java.util.ArrayList;
import java.util.List;

public class AnimalFactory {
    private final AnimalConfiguration animalConfiguration;
    private final RandomWrapper random;

    AnimalFactory(AnimalConfiguration animalConfiguration, RandomWrapper random) {
        this.animalConfiguration = animalConfiguration;
        this.random = random;
    }

    public AnimalFactory(AnimalConfiguration animalConfiguration) {
        this(animalConfiguration, new RandomWrapper());
    }

    public Animal createAnimal(Vector2d position, int birthDay) {
        var genome = createGenome();
        return new Animal(animalConfiguration.getStartEnergy(), position, genome, birthDay);
    }

    public Animal birthAnimal(Animal parent1, Animal parent2, int startBirthEnergy, int birthDay) throws AnimalNotBirthException {
        var dominating = parent1.getEnergy() > parent2.getEnergy() ? parent1 : parent2;
        var other = dominating == parent1 ? parent2 : parent1;
        var percentage = ((double) dominating.getEnergy()) / (other.getEnergy() + dominating.getEnergy());
        var dominatingLeft = random.nextInt(2) == 1;

        try {
            var childGenome = createChildGenome(dominating, other, dominatingLeft, percentage);
            var newAnimal = new Animal(startBirthEnergy, parent1.getPosition(), childGenome, parent1, parent2, birthDay);
            parent1.addChild(newAnimal);
            parent2.addChild(newAnimal);
            return newAnimal;
        } catch (InvalidCountException e) {
            throw new AnimalNotBirthException(e.getMessage());
        }

    }

    private Genome createChildGenome(Animal dominating, Animal other, boolean dominatingLeft, double percentage) throws InvalidCountException {
        var dominatingGensSize = (int) Math.ceil(percentage * animalConfiguration.getGenomeLength());
        var otherGensSize = animalConfiguration.getGenomeLength() - dominatingGensSize;

        var childGens = new ArrayList<Gen>();
        childGens.addAll(dominating.getPartOfGens(dominatingGensSize, dominatingLeft));
        childGens.addAll(other.getPartOfGens(otherGensSize, !dominatingLeft));
        var childGenome = new Genome(childGens, animalConfiguration.getMutationVariant());
        childGenome.mutate(random.nextInt(
                animalConfiguration.getMinimumMutationCount(),
                animalConfiguration.getMaximumMutationCount() + 1));

        return childGenome;
    }

    private Genome createGenome() {
        List<Gen> gens = new ArrayList<>();
        for (int i = 0; i < animalConfiguration.getGenomeLength(); i++) {
            gens.add(new Gen(random.nextInt(0, MapDirection.values().length)));
        }

        return new Genome(gens, animalConfiguration.getMutationVariant());
    }
}
