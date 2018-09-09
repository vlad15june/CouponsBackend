package org.coupons;

import org.coupons.handlers.admin.AdminCreateCompanyHandlerPost;
import org.coupons.handlers.admin.AdminCreateCustomerHandlerPost;
import org.coupons.handlers.admin.AdminGetCompaniesHandlerGet;
import org.coupons.handlers.admin.AdminGetCustomersHandlerGet;
import org.coupons.handlers.admin.AdminRemoveCompanyHandlerDelete;
import org.coupons.handlers.admin.AdminRemoveCustomerHandlerDelete;
import org.coupons.handlers.admin.AdminUpdateCompanyHandlerPut;
import org.coupons.handlers.admin.AdminUpdateCustomerHandlerPut;
import org.coupons.handlers.company.CompanyCreateCouponHandlerPost;
import org.coupons.handlers.company.CompanyGetCouponsCategoryHandlerGet;
import org.coupons.handlers.company.CompanyGetCouponsHandlerGet;
import org.coupons.handlers.company.CompanyGetCouponsPriceHandlerGet;
import org.coupons.handlers.company.CompanyRemoveCouponHandlerDelete;
import org.coupons.handlers.company.CompanySelfDetailsHandlerGet;
import org.coupons.handlers.company.CompanyUpdateCouponHandlerPut;
import org.coupons.handlers.customer.CustomerGetCouponsCategoryHnadlerGet;
import org.coupons.handlers.customer.CustomerGetCouponsHandlerGet;
import org.coupons.handlers.customer.CustomerGetCouponsPriceHandlerGet;
import org.coupons.handlers.customer.CustomerGetPurchasedCategoryHandlerGet;
import org.coupons.handlers.customer.CustomerGetPurchasedHandlerGet;
import org.coupons.handlers.customer.CustomerGetPurchasedPriceHandlerGet;
import org.coupons.handlers.customer.CustomerPurchaseCouponHandlerPost;
import org.coupons.handlers.customer.CustomerSelfDetailsHandlerGet;
import org.coupons.handlers.login.LoginHandlerPost;
import org.coupons.handlers.login.RefreshHandlerPost;
import org.coupons.util.Constants;

import com.networknt.handler.HandlerProvider;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;


public class PathHandlerProvider implements HandlerProvider {

	@Override
	public HttpHandler getHandler() {

		return Handlers.routing()

		// -------------
		// LOGIN
		// -------------
		.add(Methods.POST, "/coupons/login", new LoginHandlerPost())

		// -------------
		// REFRESH
		// -------------
		.add(Methods.POST, Constants.REFRESH_END_POINT, new RefreshHandlerPost())
		
		// -------------
		// ADMIN
		// -------------
		.add(Methods.GET, "/coupons/admin/companies", new AdminGetCompaniesHandlerGet())
		.add(Methods.GET, "/coupons/admin/customers", new AdminGetCustomersHandlerGet())
		.add(Methods.POST, "/coupons/admin/companies", new AdminCreateCompanyHandlerPost())
		.add(Methods.POST, "/coupons/admin/customers", new AdminCreateCustomerHandlerPost())
		.add(Methods.PUT, "/coupons/admin/companies", new AdminUpdateCompanyHandlerPut())
		.add(Methods.PUT, "/coupons/admin/customers", new AdminUpdateCustomerHandlerPut())
		.add(Methods.DELETE, "/coupons/admin/companies/del/{id}",
		new AdminRemoveCompanyHandlerDelete())
		.add(Methods.DELETE, "/coupons/admin/customers/del/{id}",
		new AdminRemoveCustomerHandlerDelete())

		// -------------
		// Customer
		// -------------
		.add(Methods.GET, "/coupons/customer/all", new CustomerGetCouponsHandlerGet())
		.add(Methods.GET, "/coupons/customer/all/category",
		new CustomerGetCouponsCategoryHnadlerGet())
		.add(Methods.GET, "/coupons/customer/all/price", new CustomerGetCouponsPriceHandlerGet())
		.add(Methods.GET, "/coupons/customer/purchased", new CustomerGetPurchasedHandlerGet())
		.add(Methods.GET, "/coupons/customer/purchased/category",
		new CustomerGetPurchasedCategoryHandlerGet())
		.add(Methods.GET, "/coupons/customer/purchased/price",
		new CustomerGetPurchasedPriceHandlerGet())
		.add(Methods.GET, "/coupons/customer", new CustomerSelfDetailsHandlerGet())
		.add(Methods.POST, "/coupons/customer", new CustomerPurchaseCouponHandlerPost())

		// -------------
		// Company
		// -------------
		.add(Methods.GET, "/coupons/company/all", new CompanyGetCouponsHandlerGet())
		.add(Methods.GET, "/coupons/company/all/category",
		new CompanyGetCouponsCategoryHandlerGet())
		.add(Methods.GET, "/coupons/company/all/price", new CompanyGetCouponsPriceHandlerGet())
		.add(Methods.GET, "/coupons/company", new CompanySelfDetailsHandlerGet())
		.add(Methods.POST, "/coupons/company", new CompanyCreateCouponHandlerPost())
		.add(Methods.PUT, "/coupons/company", new CompanyUpdateCouponHandlerPut())
		.add(Methods.DELETE, "/coupons/company", new CompanyRemoveCouponHandlerDelete());
	}
}
