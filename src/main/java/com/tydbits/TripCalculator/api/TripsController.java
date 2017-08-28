package com.tydbits.TripCalculator.api;

import com.tydbits.TripCalculator.dto.Trip;
import com.tydbits.TripCalculator.model.TripsRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.tydbits.TripCalculator.dto.DtoUtils.mapToArray;

@RestController
@Validated
@RequestMapping(path = "/api/v1/trips")
@Api(tags = "trips")
public class TripsController {

    @Autowired
    private TripsRepository repo;

    @RequestMapping(path = "", method = RequestMethod.GET)
    @ResponseBody
    public Trip[] list() {
        return mapToArray(repo.list(), Trip::fromModel, Trip[]::new);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public Trip get(@PathVariable int id) {
        return Trip.fromModel(repo.get(id));
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseBody
    public Trip post(@RequestBody @Valid Trip trip) {
        return Trip.fromModel(repo.add(trip.toModel()));
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Trip put(@PathVariable int id, @RequestBody @Valid Trip trip) {
        return Trip.fromModel(repo.put(id, trip.toModel()));
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
