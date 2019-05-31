package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job myJob = jobData.findById(id);
        model.addAttribute("job", myJob);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, RedirectAttributes redirectAttrs,
                      @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        }

        // get new Job specifics from JobForm
        String name = jobForm.getName();
        Employer employer = jobData.getEmployers().findById( jobForm.getEmployerId() );
        Location location = jobData.getLocations().findById( jobForm.getLocationId() );
        PositionType positionType = jobData.getPositionTypes().findById( jobForm.getPositionTypeId() );
        CoreCompetency skill = jobData.getCoreCompetencies().findById( jobForm.getCoreCompId() );

        // add new Job to JobData
        Job newJob = new Job(name, employer, location, positionType, skill);
        jobData.add(newJob);

        // save id
        redirectAttrs.addAttribute("id", newJob.getId());
        return "redirect:";

    }
}
