library(triangle)
 
generateSamples <- function(n, a, b, c) {
    return(rtriangle(n, a, b, c))
}
 
gaussian <- function(t){
    return((1/sqrt(2*pi))*exp(-1/2*(t^2)));
}
 
epanechnikov <- function(t) {
    if(t >= -1 && t <= 1) {
        return(3/4*(1-t^2));
    } else {
        return(0);
    }
}
 
windowFunction <- function(x, kernel, windowSize, volume) {
    return((1 / volume) * kernel(x / windowSize))
}
 
estimateDensity <- function(samples,
                    from, to, m, kernel, h) {
    kernelfn <- kernel
 
    sampleCount <- length(samples);
 
    stepSize = (to - from) / m
 
    curX <- from
    densities <- list()
    while(curX <= to) {
        curDensity = 0     
        for(sample in samples) {
            # Volume = 1/h^Dimensions (here: 1/h)
            curDensity <- curDensity + windowFunction(curX - sample, kernelfn, h, h)
        }
        curDensity <- curDensity / sampleCount
        densities <- c(densities, list(curDensity))
        curX <- curX + stepSize    
    }
 
    return(densities)
}
