import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LicenceUpdateControllerTest {

    @Mock
    private LicenceAPIService licenceAPIService;

    @InjectMocks
    private LicenceUpdateController licenceUpdateController; // Replace with the actual class name

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidate_withUsedControllingLine() {
        // Arrange
        Licence licence = new Licence(); // Mock or create a test licence
        ReferenceData referenceData = new ReferenceData(); // Mock or create reference data

        ControllingLine controllingLineUsed = mock(ControllingLine.class);
        when(controllingLineUsed.hasBeenUsed()).thenReturn(true);
        when(controllingLineUsed.getLineNumber()).thenReturn(42);

        ControllingLine controllingLineUnused = mock(ControllingLine.class);
        when(controllingLineUnused.hasBeenUsed()).thenReturn(false);

        List<ControllingLine> controllingLines = Arrays.asList(controllingLineUsed, controllingLineUnused);

        Licence foundLicence = mock(Licence.class);
        when(foundLicence.getControllingLines()).thenReturn(controllingLines);

        when(licenceAPIService.findExistingLicence(licence))
                .thenReturn(java.util.Optional.of(foundLicence));

        // Act
        List<LicenceUpdateError> errors = licenceUpdateController.validate(licence, referenceData);

        // Assert
        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE", errors.get(0).getCode()); // Replace with actual error code
        assertEquals("Cannot replace licence with missing licence line that has been used.", errors.get(0).getMessage());
        assertEquals("/licence/42/lineNumber", errors.get(0).getPath());

        // Verify logs or any other expected behavior
        verify(licenceAPIService, times(1)).findExistingLicence(licence);
        verify(controllingLineUsed, times(1)).hasBeenUsed();
    }

    @Test
    void testValidate_withNoUsedControllingLines() {
        // Arrange
        Licence licence = new Licence();
        ReferenceData referenceData = new ReferenceData();

        ControllingLine controllingLineUnused = mock(ControllingLine.class);
        when(controllingLineUnused.hasBeenUsed()).thenReturn(false);

        List<ControllingLine> controllingLines = Collections.singletonList(controllingLineUnused);

        Licence foundLicence = mock(Licence.class);
        when(foundLicence.getControllingLines()).thenReturn(controllingLines);

        when(licenceAPIService.findExistingLicence(licence))
                .thenReturn(java.util.Optional.of(foundLicence));

        // Act
        List<LicenceUpdateError> errors = licenceUpdateController.validate(licence, referenceData);

        // Assert
        assertEquals(0, errors.size());

        // Verify behavior
        verify(licenceAPIService, times(1)).findExistingLicence(licence);
    }
}