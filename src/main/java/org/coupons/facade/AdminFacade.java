package org.coupons.facade;

import java.sql.SQLException;
import java.util.List;

import org.coupons.dbo.CompanyDAO;
import org.coupons.dbo.CustomerDAO;
import org.coupons.pojo.Company;
import org.coupons.pojo.Customer;
import org.coupons.pojo.Role;
import org.coupons.security.AuthHelper;


public class AdminFacade {

	// COMPANY METHODS

	public static Company addCompany(final Company company, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CompanyDAO.addCompany(company);
		return null;
	}

	public static Company updateCompany(final Company company, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CompanyDAO.updateCompany(company);
		return null;
	}

	public static void deleteCompany(final String companyId, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			CompanyDAO.deleteCompany(companyId);
	}

	public static List<Company> getAllCompanies(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CompanyDAO.getAllCompanies();
		return null;
	}

	public static Company getCompany(final String companyId, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CompanyDAO.getOneCompany(companyId);
		return null;
	}

	// CUSTOMER METHODS

	public static Customer addCustomer(final Customer customer, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CustomerDAO.addCustomer(customer);
		return null;
	}

	public static Customer updateCustomer(final Customer customer, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CustomerDAO.updateCustomer(customer);
		return null;
	}

	public static void deleteCustomer(final String customerId, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			CustomerDAO.deleteCustomer(customerId);
	}

	public static List<Customer> getAllCustomers(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CustomerDAO.getAllCustomers();
		return null;
	}

	public static Customer getCustomer(final String customerId, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(Role.ADMIN.name().equalsIgnoreCase(userRole))
			return CustomerDAO.getOneCustomer(customerId);
		return null;
	}

}
