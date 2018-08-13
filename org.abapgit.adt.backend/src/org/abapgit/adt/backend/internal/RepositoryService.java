package org.abapgit.adt.backend.internal;

import java.net.URI;

import org.abapgit.adt.backend.IRepositories;
import org.abapgit.adt.backend.IRepositoryService;
import org.eclipse.core.runtime.IProgressMonitor;

import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.compatibility.filter.AdtCompatibleRestResourceFilterFactory;
import com.sap.adt.compatibility.filter.IAdtCompatibleRestResourceFilter;

public class RepositoryService implements IRepositoryService {

	private final String destinationId;
	private final URI uri;

	public RepositoryService(String destinationId, URI uri) {
		this.destinationId = destinationId;
		this.uri = uri;
	}

	// new clone method with new handlers

	@Override
	public IRepositories getRepositories(IProgressMonitor monitor) {
		IRestResource restResource = AdtRestResourceFactory.createRestResourceFactory()
				.createResourceWithStatelessSession(this.uri, this.destinationId);

		IContentHandler<IRepositories> responseContentHandlerV1 = new RepositoriesContentHandlerV1();
		restResource.addContentHandler(responseContentHandlerV1);

		IAdtCompatibleRestResourceFilter compatibilityFilter = AdtCompatibleRestResourceFilterFactory
				.createFilter(responseContentHandlerV1);
		restResource.addRequestFilter(compatibilityFilter);
		restResource.addResponseFilter(compatibilityFilter);

		return restResource.get(monitor, IRepositories.class);
	}

}