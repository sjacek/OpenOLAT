package org.olat.user.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.olat.basesecurity.IdentityImpl;
import org.olat.core.id.Identity;
import org.olat.core.id.Persistable;
import org.olat.core.util.StringHelper;
import org.olat.user.UserDataExport;

/**
 * 
 * Initial date: 23 mai 2018<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
@Entity(name="userdataexport")
@Table(name="o_user_data_export")
public class UserDataExportImpl implements UserDataExport, Persistable {

	private static final long serialVersionUID = 3359965105813614815L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true, insertable=true, updatable=false)
	private Long key;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creationdate", nullable=false, insertable=true, updatable=false)
	private Date creationDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastmodified", nullable=false, insertable=true, updatable=true)
	private Date lastModified;
	
	@Column(name="u_directory", nullable=true, insertable=true, updatable=false)
	private String directory;
	@Column(name="u_status", nullable=true, insertable=true, updatable=true)
	private String statusString;
	@Column(name="u_export_ids", nullable=true, insertable=true, updatable=true)
	private String exporterIdList;
	
	@ManyToOne(targetEntity=IdentityImpl.class,fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="fk_identity", nullable=true, insertable=true, updatable=false)
    private Identity identity;

	@Override
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	@Override
	public void setLastModified(Date date) {
		lastModified = date;
	}

	@Override
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	@Override
	public ExportStatus getStatus() {
		return StringHelper.containsNonWhitespace(statusString)
				? ExportStatus.valueOf(statusString) : ExportStatus.none;
	}

	@Override
	public void setStatus(ExportStatus status) {
		if(status == null) {
			statusString = ExportStatus.none.name();
		} else {
			statusString = status.name();
		}
	}

	@Override
	@Transient
	public Set<String> getExportIds() {
		if(StringHelper.containsNonWhitespace(exporterIdList)) {
			String[] ids = exporterIdList.split("[,]");
			Set<String> idSet = new HashSet<>();
			for(String id:ids) {
				idSet.add(id);
			}
			return idSet;
		}
		return Collections.emptySet();
	}

	public String getExporterIdList() {
		return exporterIdList;
	}

	public void setExporterIdList(String exporterIdList) {
		this.exporterIdList = exporterIdList;
	}

	@Override
	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	@Override
	public int hashCode() {
		return getKey() == null ? 2387646 : getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof UserDataExportImpl) {
			UserDataExportImpl data = (UserDataExportImpl)obj;
			return getKey() != null && getKey().equals(data.getKey());
		}
		return super.equals(obj);
	}

	@Override
	public boolean equalsByPersistableKey(Persistable persistable) {
		return equals(persistable);
	}
}