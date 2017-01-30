import numpy
import matplotlib.pyplot as plot
import matplotlib.mlab as mlab


def get_univariant_deviation_prob(mean, x):
    return 1 / (numpy.sqrt(2 * numpy.pi)) * numpy.exp(-1 * numpy.power((x - mean), 2) / 2)


def get_multivariant_deviation_prob(means, x):
    prob = 0
    for index, mean in enumerate(means):
        prob += get_univariant_deviation_prob(means[index], x)
    return prob


def get_posterior_prob(means, x, probs, class_index):
    lower_value = 0
    for i, mean in enumerate(means):
        lower_value += get_univariant_deviation_prob(mean, x) * probs[i]

    upper_value = get_univariant_deviation_prob(means[class_index], x) * probs[class_index]

    return upper_value / lower_value

def maximum_likelyhood_iteration(starting_means, x_values, class_probs, steps):
    means = list(starting_means)
    for j in range(0, steps):
        print("#"*50)
        print("calculating step " + str(j))
        print("#" * 50)
        for i, val in enumerate(means):
            tmp = 0
            upper_sum = 0
            lower_sum = 0
            for x in x_values:
                posterior_prob = get_posterior_prob(means, x, class_probs, i)
                upper_sum += posterior_prob * x
                lower_sum += posterior_prob
            means[i] = upper_sum / lower_sum
            print("updated my_" + str(i + 1) + " to " + str(means[i]) + " with " + str(upper_sum) + "/" + str(lower_sum))

        msg = "mean vector is now ("
        first = True
        for mean in means:
            if first:
                first = False
            else:
                msg += ", "
            msg += str(mean)
        msg += ")"
        print(msg)

    return means


starting_means = (-.35, .5)
class_probs = (.25, .75)
means = maximum_likelyhood_iteration(starting_means, (-2.25, 1.07, .54, 2.4, 2.57, 1.4), class_probs, 100)
xs = numpy.linspace(-5, 5, 1000)
for i, mean in enumerate(means):
    plot.plot(xs, mlab.normpdf(xs, mean, 1) * class_probs[i])
plot.show()

