package com.spring.kushal.jwtsecuritymongodb.service;

import java.util.Date;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.ClientSession;
import com.spring.kushal.jwtsecuritymongodb.exception.TourException;
import com.spring.kushal.jwtsecuritymongodb.models.Balance;
import com.spring.kushal.jwtsecuritymongodb.models.Expenditure;
import com.spring.kushal.jwtsecuritymongodb.models.ImageModel;
import com.spring.kushal.jwtsecuritymongodb.models.Tour;
import com.spring.kushal.jwtsecuritymongodb.models.User;
import com.spring.kushal.jwtsecuritymongodb.repository.ExpenditureRepository;
import com.spring.kushal.jwtsecuritymongodb.repository.TourRepository;
import com.spring.kushal.jwtsecuritymongodb.repository.UserRepository;

@Service
public class TourServiceImpl implements TourService {

	@Autowired
	TourRepository tourRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ExpenditureRepository expenditureRepo;
	
	ImageModelService imgModelService;
	
	@Override
	public Tour getTourByID(String id) throws TourException {
		Optional<Tour> tour= tourRepo.findById(id);
		//System.out.println(tour.get());
		if(tour.isPresent()) {
			User createdByUser= tour.get().getCreatedBy();
			createdByUser.setTours(null);
			tour.get().setCreatedBy(createdByUser);
			//tour.setParticipants(null);
			Set<User> part= tour.get().getParticipants();
			//System.out.println(tour.get().getParticipants());
			for(User u:part) {
//				 ImageModel img = new ImageModel(u.getProfilePicture().getName(), u.getProfilePicture().getType(),
//		                 imgModelService.decompressBytes(u.getProfilePicture().getPicByte()));
//				 u.setProfilePicture(img);
				 u.setProfilePicture(null);
				u.setTours(null);
			}
			tour.get().setParticipants(part);
			return tour.get();
		}
		else {
			throw new TourException("Tour with id:"+id+" not found");
		}

	}

	@Override
	public List<Tour> getAllTours() throws TourException {
		List<Tour> tours= tourRepo.findAll();
		if(tours.size()>0) {
			return tours;
		}
		else {
			throw new TourException("no tours found");
		}
	}


	@Override
	@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
	public void createTour(Tour tour) throws ConstraintViolationException {
		try {
			tourRepo.save(tour);
			
			for(User participant:tour.getParticipants()) {
			// userRepo.findById(participant.getId());
				Balance balance=new Balance(participant,0,0,0);
				//System.out.println(participant.toString());
				tour.addBalance(balance);
				Optional<User> participantOptional= userRepo.findById(participant.getId());
				User participantToSave=participantOptional.get();
				participantToSave.addTours(tour);
				tour.setDate(new Date(System.currentTimeMillis()));
				userRepo.save(participantToSave);
			}
			tourRepo.save(tour);	
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new RuntimeException("error occured in transaction ");
		}
	}

	

	@Override
	@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
	public void addBill(User spentBy, double amountSpent, String tourId, Set<User> billSpenton, String billNote)
			throws TourException, ConstraintViolationException {
		try {
			Date dateString=new Date(System.currentTimeMillis());
		//	Expenditure expenditure=new Expenditure(spentBy,amountSpent,billNote,spenton );
			Expenditure expenditure=new Expenditure();
			expenditure.setAmountSpent(amountSpent);
			expenditure.setSpentBy(spentBy);
			expenditure.setBillNote(billNote);
			expenditure.setSplitBillOn(billSpenton);
			expenditure.setBillDate(new Date(System.currentTimeMillis()));
			expenditureRepo.save(expenditure);
			
			Tour tour =getTourByID(tourId);
			int billSpentOnSize=billSpenton.size();
			double share=(100*(amountSpent/billSpentOnSize))/100;
			boolean calculateSpentOn=true;
			System.out.println(tour);
			for(User user:billSpenton) {
				for(Balance balance:tour.getBalance()) {
					if(balance.getUser().getId().equals(user.getId())) {
						balance.setBalance(balance.getBalance()-share);
						balance.setShare(balance.getShare()+share);
						tour.addBalance(balance);
						//tourRepo.save(tour);
					}
					if(balance.getUser().getId().equals(spentBy.getId())&& calculateSpentOn) {
						balance.setBalance(balance.getBalance()+amountSpent);
						
						balance.setTotalAmountSpent(balance.getTotalAmountSpent()+amountSpent);
						calculateSpentOn=false;
						tour.addBalance(balance);
						
					}
				}
			}
			tour.addExpentiture(expenditure);
			tourRepo.save(tour);
			System.out.println(tour);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public Tour findTourBrief(String id) throws TourException {
		
		return tourRepo.findTourBrief(id).get();
		
	}

	@Override
	public boolean endTour(String id) throws TourException {
		Tour tour=tourRepo.findById(id).get();
		if(tour!=null) {
			tour.setActive(false);
			Tour status = tourRepo.save(tour);
			return status!=null?true:false;
		}
		else {
			throw new TourException(id);
		}
		
	}

}
